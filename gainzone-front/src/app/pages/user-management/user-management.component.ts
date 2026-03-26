import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule, Users, UserCog, Trash2, Edit, Search, Shield, Mail, Phone, X } from 'lucide-angular';
import { UserService } from '../../services/user.service';
import { User, UserUpdateRequest } from '../../models/user.model';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './user-management.component.html'
})
export class UserManagementComponent implements OnInit {
  users = signal<User[]>([]);
  searchQuery = signal('');
  errorMessage = '';

  // Modal State
  showEditModal = false;
  selectedUser: User | null = null;
  editForm: UserUpdateRequest = {};

  // Icons
  readonly icons = { Users, UserCog, Trash2, Edit, Search, Shield, Mail, Phone, X };

  // Computed filtered users
  filteredUsers = computed(() => {
    const query = this.searchQuery().toLowerCase();
    return this.users().filter(user => 
      user.name?.toLowerCase().includes(query) || 
      user.lastName?.toLowerCase().includes(query) ||
      user.email?.toLowerCase().includes(query) ||
      user.username?.toLowerCase().includes(query)
    );
  });

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data) => this.users.set(data),
      error: (err) => {
        console.error('Erreur chargement utilisateurs', err);
        this.errorMessage = 'Impossible de charger la liste des utilisateurs.';
      }
    });
  }

  openEditModal(user: User): void {
    this.selectedUser = user;
    this.editForm = {
      username: user.username,
      email: user.email,
      name: user.name,
      lastName: user.lastName,
      phone: user.phone,
      role: user.role
    };
    this.showEditModal = true;
  }

  closeModal(): void {
    this.showEditModal = false;
    this.selectedUser = null;
  }

  onUpdateUser(): void {
    if (!this.selectedUser) return;

    this.userService.updateUser(this.selectedUser.id, this.editForm).subscribe({
      next: () => {
        this.loadUsers();
        this.closeModal();
      },
      error: (err) => alert("Erreur lors de la mise à jour de l'utilisateur.")
    });
  }

  deleteUser(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ? Cette action est irréversible.')) {
      this.userService.deleteUser(id).subscribe({
        next: () => this.loadUsers(),
        error: (err) => alert("Erreur lors de la suppression de l'utilisateur.")
      });
    }
  }

  getRoleBadgeClass(role: string): string {
    switch (role) {
      case 'ADMIN': return 'bg-volt/10 text-volt border border-volt/20';
      case 'COACH': return 'bg-purple-500/10 text-purple-500 border border-purple-500/20';
      case 'MEMBER': return 'bg-blue-500/10 text-blue-500 border border-blue-500/20';
      default: return 'bg-zinc-800 text-dim-gray';
    }
  }
}
