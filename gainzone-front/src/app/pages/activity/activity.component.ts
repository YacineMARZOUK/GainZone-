import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivityService } from '../../services/activity.service';
import { Activity } from '../../models/activity.model';
import { LucideAngularModule, Calendar, Clock, User, Users } from 'lucide-angular';

@Component({
  selector: 'app-activity',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  providers: [DatePipe],
  templateUrl: './activity.component.html'
})
export class ActivityComponent implements OnInit {
  activities: Activity[] = [];
  joinedActivities: Set<number> = new Set<number>();
  errorMessage = '';

  icons = {
    Calendar,
    Clock,
    User,
    Users
  };

  constructor(private activityService: ActivityService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadActivities();
  }

  loadActivities(): void {
    this.activityService.getActivities().subscribe({
      next: (data) => {
        console.log('Activités reçues :', data);
        this.activities = data;
        this.cdr.detectChanges(); // Force le rafraîchissement de la vue
      },
      error: (err) => {
        console.error('Erreur de chargement des activités', err);
        this.errorMessage = 'Impossible de charger les activités.';
      }
    });
  }

  joinActivity(activityId: number): void {
    if (this.joinedActivities.has(activityId)) return;

    this.activityService.joinActivity(activityId).subscribe({
      next: (updatedActivity) => {
        // Mettre à jour l'activité dans la liste
        const index = this.activities.findIndex(a => a.id === activityId);
        if (index !== -1) {
          this.activities[index] = updatedActivity;
        }
        this.joinedActivities.add(activityId);
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors de la réservation.vous etes deja inscrit';
      }
    });
  }

  getProgressBarWidth(current: number, max: number): string {
    if (!max || max === 0) return '0%';
    const percentage = (current / max) * 100;
    return `${Math.min(percentage, 100)}%`;
  }
}
