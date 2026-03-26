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
  
  isEditing = false;
  editingId: number | null = null;
  
  // New/Edit Activity Form
  newActivity = {
    name: '',
    description: '',
    type: 'CROSSFIT',
    dateTime: '',
    durationMinutes: 60,
    maxParticipants: 15
  };

  participants: any[] = [];
  isLoadingParticipants = false;

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
    this.isEditing = false;
    this.editingId = null;
    this.showCreateModal = true;
    
    // Set default date to tomorrow
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    tomorrow.setHours(18, 0, 0, 0);
    
    this.newActivity = {
      name: '',
      description: '',
      type: 'CROSSFIT',
      dateTime: tomorrow.toISOString().slice(0, 16),
      durationMinutes: 60,
      maxParticipants: 15
    };
  }

  openEditModal(activity: Activity): void {
    this.isEditing = true;
    this.editingId = activity.id;
    this.showCreateModal = true;
    
    // Convert to ISO string for datetime-local
    const d = new Date(activity.dateTime);
    // Pad functions to ensure 2 digits
    const pad = (n: number) => n.toString().padStart(2, '0');
    const isoDateTime = `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
    
    this.newActivity = {
      name: activity.name,
      description: activity.description || '',
      type: activity.type || 'CROSSFIT',
      dateTime: isoDateTime,
      durationMinutes: activity.durationMinutes,
      maxParticipants: activity.maxParticipants
    };
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
  }

  onSubmitCreate(): void {
    const request = {
      ...this.newActivity
    };
    
    if (this.isEditing && this.editingId) {
      this.activityService.updateActivity(this.editingId, request).subscribe({
        next: () => {
          this.closeCreateModal();
          this.loadActivities(); // Raffraîchissement automatique
        },
        error: (err) => {
          console.error("Modification échouée", err);
          alert("Erreur lors de la modification de la séance.");
        }
      });
    } else {
      this.activityService.createActivity(request).subscribe({
        next: () => {
          this.closeCreateModal();
          this.loadActivities(); // Raffraîchissement automatique
        },
        error: (err) => {
          console.error("Création échouée", err);
          alert("Erreur lors de la création de la séance.");
        }
      });
    }
  }

  deleteActivity(id: number): void {
    if(!confirm('Êtes-vous sûr de vouloir supprimer cette séance ?')) return;

    this.activityService.deleteActivity(id).subscribe({
      next: () => {
        this.loadActivities(); // Raffraîchissement automatique
      },
      error: (err) => console.error("Erreur suppression", err)
    });
  }

  openParticipants(activity: Activity): void {
    this.selectedActivity = activity;
    this.showParticipantsModal = true;
    this.isLoadingParticipants = true;
    
    this.activityService.getMembersByActivityId(activity.id).subscribe({
      next: (data) => {
        this.participants = data;
        this.isLoadingParticipants = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Erreur chargement membres", err);
        this.participants = []; // Modal sera vide
        this.isLoadingParticipants = false;
        this.cdr.detectChanges();
      }
    });
  }

  closeParticipants(): void {
    this.showParticipantsModal = false;
    this.selectedActivity = null;
    this.participants = [];
  }
}
