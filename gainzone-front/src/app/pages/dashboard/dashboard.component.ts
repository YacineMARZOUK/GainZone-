import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { MemberService } from '../../services/member.service';
import { MemberProfileResponse } from '../../models/member.model';
import { LucideAngularModule, Weight, Target, Activity } from 'lucide-angular';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, LucideAngularModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  username: string | null = '';
  role: string | null = '';
  
  profileForm!: FormGroup;
  profileData: MemberProfileResponse | null = null;
  isLoading = false;
  successMessage = '';
  errorMessage = '';

  // Icons used in template
  icons = {
    Weight,
    Target,
    Activity
  };

  constructor(
    private authService: AuthService,
    private memberService: MemberService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.username = this.authService.getUsername();
    this.role = this.authService.getRole();

    this.initForm();

    if (this.role === 'MEMBER') {
      this.loadProfile();
    }
  }

  initForm(): void {
    this.profileForm = this.fb.group({
      age: [null, [Validators.min(0)]],
      gender: [''], // M ou F
      weight: [null, [Validators.min(0)]],
      height: [null, [Validators.min(0)]],
      goal: [''], // WEIGHT_LOSS, MUSCLE_GAIN, ENDURANCE, MAINTENANCE
      fitnessLevel: [''], // BEGINNER, INTERMEDIATE, ADVANCED
    });
  }

  loadProfile(): void {
    this.memberService.getProfile().subscribe({
      next: (data) => {
        this.profileData = data;
        this.profileForm.patchValue({
          age: data.age,
          gender: data.gender,
          weight: data.weight,
          height: data.height,
          goal: data.goal,
          fitnessLevel: data.fitnessLevel
        });
      },
      error: (err) => console.error('Erreur lors du chargement du profil', err)
    });
  }

  onSubmit(): void {
    if (this.profileForm.valid && this.role === 'MEMBER') {
      this.isLoading = true;
      this.successMessage = '';
      this.errorMessage = '';

      this.memberService.updateProfile(this.profileForm.value).subscribe({
        next: (data) => {
          this.isLoading = false;
          this.profileData = data;
          this.successMessage = 'Profil mis à jour avec succès !';
          setTimeout(() => this.successMessage = '', 3000);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Erreur lors de la mise à jour.';
          console.error(err);
        }
      });
    }
  }

  getDisplayGoal(goal: string | undefined): string {
    if (!goal) return 'Non défini';
    const goals: { [key: string]: string } = {
      'WEIGHT_LOSS': 'Perte de poids',
      'MUSCLE_GAIN': 'Prise de masse',
      'ENDURANCE': 'Endurance',
      'MAINTENANCE': 'Maintien'
    };
    return goals[goal] || goal;
  }

  getDisplayLevel(level: string | undefined): string {
    if (!level) return 'Non défini';
    const levels: { [key: string]: string } = {
      'BEGINNER': 'Débutant',
      'INTERMEDIATE': 'Intermédiaire',
      'ADVANCED': 'Avancé'
    };
    return levels[level] || level;
  }
}
