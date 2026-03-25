import { Component, OnInit } from '@angular/core';
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

  constructor(private activityService: ActivityService) { }

  ngOnInit(): void {
    this.loadActivities();
  }

  loadActivities(): void {
    this.activityService.getActivities().subscribe({
      next: (data) => {
        console.log('Activités reçues :', data);
        this.activities = data;
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
        console.error('Erreur lors de la réservation', err);
        this.errorMessage = 'Erreur lors de la réservation. La séance est peut-être complète.';
      }
    });
  }

  getProgressBarWidth(current: number, max: number): string {
    if (!max || max === 0) return '0%';
    const percentage = (current / max) * 100;
    return `${Math.min(percentage, 100)}%`;
  }
}
