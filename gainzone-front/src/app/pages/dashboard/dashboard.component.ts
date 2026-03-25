import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { MemberService } from '../../services/member.service';
import { AiService } from '../../services/ai.service';
import { MemberProfileResponse } from '../../models/member.model';
import { AITrainingPlanResponse } from '../../models/ai.model';
import { LucideAngularModule, Weight, Target, Activity, Sparkles } from 'lucide-angular';
import { finalize } from 'rxjs/operators';

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

  // AI properties
  aiPlan: AITrainingPlanResponse | null = null;
  isAiLoading = false;
  aiErrorMessage = '';

  // Icons used in template
  icons = {
    Weight,
    Target,
    Activity,
    Sparkles
  };

  constructor(
    private authService: AuthService,
    private memberService: MemberService,
    private aiService: AiService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
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
      goal: [''], // WEIGHT_LOSS, WEIGHT_GAIN, PERFORMANCE
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

      this.memberService.updateProfile(this.profileForm.value)
        .pipe(
          finalize(() => {
            this.isLoading = false;
            this.cdr.detectChanges();
          })
        )
        .subscribe({
          next: (data) => {
            this.profileData = data;
            this.successMessage = 'Profil mis à jour avec succès !';
            setTimeout(() => {
              this.successMessage = '';
              this.cdr.detectChanges();
            }, 3000);
          },
          error: (err) => {
            this.errorMessage = 'Erreur lors de la mise à jour.';
          }
        });
    }
  }

  generateAIPlan(): void {
    this.isAiLoading = true;
    this.aiErrorMessage = '';
    this.aiPlan = null;
    
    this.aiService.generatePlan()
      .pipe(
        finalize(() => {
          this.isAiLoading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (plan) => {
          this.aiPlan = plan;
        },
        error: (err) => {
          console.error('Erreur génération plan IA', err);
          this.aiErrorMessage = "Le GZ-AI Coaching nécessite d'avoir enregistré vos informations de profil (Objectif, Poids, etc.) auparavant, ou le service est temporairement indisponible.";
        }
      });
  }

  getDisplayGoal(goal: string | undefined): string {
    if (!goal) return 'Non défini';
    const goals: { [key: string]: string } = {
      'WEIGHT_LOSS': 'Perte de poids',
      'WEIGHT_GAIN': 'Prise de masse',
      'PERFORMANCE': 'Performance'
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
