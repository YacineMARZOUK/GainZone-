import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TrainingProgramService, TrainingProgram } from '../../services/training-program.service';
import { LucideAngularModule, Dumbbell, UserPlus, Plus, Trash, Edit, Save, X, Activity } from 'lucide-angular';

interface Exercise {
  name: string;
  sets: number;
  reps: number;
}

@Component({
  selector: 'app-program-manager',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './program-manager.component.html'
})
export class ProgramManagerComponent implements OnInit {
  programs: TrainingProgram[] = [];
  members: any[] = [];
  
  showModal = false;
  isEditing = false;
  editingId: number | null = null;
  errorMessage = '';

  form = {
    name: '',
    description: '',
    frequency: '3x/semaine',
    durationWeeks: 4,
    memberId: null as number | null
  };

  exercises: Exercise[] = [];

  icons = { Dumbbell, UserPlus, Plus, Trash, Edit, Save, X, Activity };

  constructor(
    private programService: TrainingProgramService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadPrograms();
    this.loadMembers();
  }

  loadPrograms(): void {
    this.programService.getAll().subscribe({
      next: (data) => {
        this.programs = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err)
    });
  }

  loadMembers(): void {
    this.programService.getMembers().subscribe({
      next: (data) => {
        // Filtrer pour n'obtenir que les membres (MEMBER)
        this.members = data.filter(u => u.role === 'MEMBER' || String(u.role).includes('MEMBER'));
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err)
    });
  }

  getMemberName(memberId: number): string {
    const member = this.members.find(m => m.id === memberId);
    return member ? (member.name || member.username || member.email) : 'Membre Inconnu';
  }

  getExerciseCount(json: string): number {
    try {
      const arr = JSON.parse(json || '[]');
      return arr.length;
    } catch {
      return 0;
    }
  }

  openCreateModal(): void {
    this.isEditing = false;
    this.editingId = null;
    this.form = {
      name: '',
      description: '',
      frequency: '3x/semaine',
      durationWeeks: 4,
      memberId: null
    };
    this.exercises = [{ name: '', sets: 3, reps: 10 }];
    this.showModal = true;
  }

  openEditModal(p: TrainingProgram): void {
    this.isEditing = true;
    this.editingId = p.id;
    this.form = {
      name: p.name,
      description: p.description,
      frequency: p.frequency,
      durationWeeks: p.durationWeeks,
      memberId: p.memberId
    };
    try {
      this.exercises = JSON.parse(p.exercisesJson || '[]');
      if (!this.exercises.length) this.exercises = [{ name: '', sets: 3, reps: 10 }];
    } catch {
      this.exercises = [{ name: '', sets: 3, reps: 10 }];
    }
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.errorMessage = '';
  }

  addExercise(): void {
    this.exercises.push({ name: '', sets: 3, reps: 10 });
  }

  removeExercise(index: number): void {
    this.exercises.splice(index, 1);
  }

  onSubmit(): void {
    if (!this.form.name || !this.form.memberId) {
      this.errorMessage = "Veuillez remplir le nom et assigner un membre.";
      return;
    }

    // Filtrer les exercices vides
    const validExercises = this.exercises.filter(e => e.name.trim() !== '');
    if (validExercises.length === 0) {
      this.errorMessage = "Veuillez ajouter au moins un exercice valide.";
      return;
    }

    const payload = {
      ...this.form,
      exercisesJson: JSON.stringify(validExercises)
    };

    if (this.isEditing && this.editingId) {
      this.programService.update(this.editingId, payload).subscribe({
        next: () => {
          this.loadPrograms();
          this.closeModal();
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = "Erreur de modification du programme.";
        }
      });
    } else {
      this.programService.create(payload).subscribe({
        next: () => {
          this.loadPrograms();
          this.closeModal();
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = "Erreur de création du programme.";
        }
      });
    }
  }

  deleteProgram(id: number): void {
    if(!confirm("Êtes-vous sûr de vouloir supprimer ce programme ?")) return;
    this.programService.delete(id).subscribe({
      next: () => this.loadPrograms(),
      error: (err) => console.error(err)
    });
  }
}
