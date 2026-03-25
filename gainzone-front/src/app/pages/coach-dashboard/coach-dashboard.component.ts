import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivityService } from '../../services/activity.service';
import { Activity } from '../../models/activity.model';
import { LucideAngularModule, Users, Plus, Trash, Calendar, Clock, Edit } from 'lucide-angular';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-coach-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule],
  providers: [DatePipe],
  templateUrl: './coach-dashboard.component.html'
})
export class CoachDashboardComponent implements OnInit {
  activities: Activity[] = [];
  
  // Modal state
  showCreateModal = false;
  showParticipantsModal = false;
  selectedActivity: Activity | null = null;
  
  // New Activity Form
  newActivity = {
    name: '',
    description: '',
    type: 'CROSSFIT',
    dateTime: '',
    durationMinutes: 60,
    maxParticipants: 15
  };

  // Mock participants since backend doesn't have the endpoint yet
  mockParticipants = [
    { name: 'Alexandre Dupont', email: 'alex@example.com', level: 'INTERMÉDIAIRE' },
    { name: 'Sarah Connor', email: 'sarah@example.com', level: 'AVANCÉ' },
    { name: 'Jean Valjean', email: 'jean@example.com', level: 'DÉBUTANT' }
  ];

  icons = { Users, Plus, Trash, Calendar, Clock, Edit };
  errorMessage = '';

  constructor(
    private activityService: ActivityService, 
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const role = this.authService.getRole();
    if (role !== 'COACH' && role !== 'ADMIN') {
      this.errorMessage = "Accès non autorisé. Vous devez être COACH.";
      return;
    }
    this.loadActivities();
  }

  loadActivities(): void {
    this.activityService.getActivities().subscribe({
      next: (data) => {
        // Optionnel : ne filtrer que les activités créées par ce coach si on avait son ID, 
        // ou bien afficher tout pour le dashboard.
        this.activities = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur de chargement', err);
        this.errorMessage = 'Impossible de charger les activités.';
      }
    });
  }

  getFillBadgeColor(current: number, max: number): string {
    if (!max || max === 0) return 'bg-zinc-800 text-dim-gray';
    const percent = (current / max) * 100;
    if (percent < 50) return 'bg-emerald-500/10 text-emerald-500 border border-emerald-500/20';
    if (percent > 80) return 'bg-orange-500/10 text-orange-500 border border-orange-500/20';
    return 'bg-volt/10 text-volt border border-volt/20';
  }

  getFillText(current: number, max: number): string {
    if (!max || max === 0) return 'Moy.';
    const percent = (current / max) * 100;
    if (percent < 50) return 'Places Dissos';
    if (percent > 80) return 'Presque Plein';
    return 'Remplissage Normal';
  }

  openCreateModal(): void {
    this.showCreateModal = true;
    // Set default date to tomorrow
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    tomorrow.setHours(18, 0, 0, 0);
    // Format YYYY-MM-DDTHH:mm
    this.newActivity.dateTime = tomorrow.toISOString().slice(0, 16);
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
  }

  onSubmitCreate(): void {
    // Format payload to backend specifications
    // Note : description is mapped to type for simplicity or can be added to form
    const request = {
      ...this.newActivity,
      description: 'Séance de type ' + this.newActivity.type + ' organisée par le coach. ' + this.newActivity.description
    };
    
    // Call service (assuming createActivity exists in ActivityService)
    // If not, we log what should be done
    if ((this.activityService as any).createActivity) {
      (this.activityService as any).createActivity(request).subscribe({
        next: (created: Activity) => {
          this.activities.unshift(created);
          this.closeCreateModal();
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          console.error("Création échouée", err);
          alert("Erreur lors de la création de la séance.");
        }
      });
    } else {
      console.warn("createActivity method not found in service. Simulating creation.", request);
      // Simulate
      this.activities.unshift({
        id: Date.now(),
        ...this.newActivity,
        description: 'New Description',
        currentParticipantsCount: 0,
        coachId: 999,
        coachName: 'Moi'
      });
      this.closeCreateModal();
    }
  }

  deleteActivity(id: number): void {
    if(!confirm('Êtes-vous sûr de vouloir supprimer cette séance ?')) return;

    if ((this.activityService as any).deleteActivity) {
      (this.activityService as any).deleteActivity(id).subscribe({
        next: () => {
          this.activities = this.activities.filter(a => a.id !== id);
          this.cdr.detectChanges();
        },
        error: (err: any) => console.error("Erreur suppression", err)
      });
    } else {
      console.warn("deleteActivity non implémenté. Simulation...");
      this.activities = this.activities.filter(a => a.id !== id);
    }
  }

  openParticipants(activity: Activity): void {
    this.selectedActivity = activity;
    this.showParticipantsModal = true;
  }

  closeParticipants(): void {
    this.showParticipantsModal = false;
    this.selectedActivity = null;
  }
}
