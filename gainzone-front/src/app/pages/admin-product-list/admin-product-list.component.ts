import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminProductService, Product } from '../../services/admin-product.service';
import { AuthService } from '../../services/auth.service';
import { LucideAngularModule, Package, PlusCircle, Trash2, Edit } from 'lucide-angular';

@Component({
  selector: 'app-admin-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './admin-product-list.component.html'
})
export class AdminProductListComponent implements OnInit {
  products = signal<Product[]>([]);
  errorMessage = '';

  // Modal State
  showModal = false;
  isEditing = false;
  editingId: number | null = null;
  
  // Form Data
  currentProduct: any = {
    name: '',
    description: '',
    price: 0,
    stockQuantity: 0,
    category: 'VÊTEMENTS',
    imageUrl: ''
  };

  icons = { Package, PlusCircle, Trash2, Edit };

  constructor(
    private productService: AdminProductService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const role = this.authService.getRole();
    if (role !== 'ADMIN') {
      this.errorMessage = "Accès non autorisé. Vous devez être ADMIN.";
      return;
    }
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
        console.log('Produits chargés:', data);
        this.products.set(data);
      },
      error: (err) => {
        console.error('Erreur chargement produits', err);
        this.errorMessage = 'Impossible de charger la boutique.';
      }
    });
  }

  openCreateModal(): void {
    this.isEditing = false;
    this.editingId = null;
    this.currentProduct = {
      name: '',
      description: '',
      price: 0,
      stockQuantity: 0,
      category: 'VÊTEMENTS',
      imageUrl: ''
    };
    this.showModal = true;
  }

  openEditModal(product: Product): void {
    this.isEditing = true;
    this.editingId = product.id;
    this.currentProduct = {
      name: product.name,
      description: product.description || '',
      price: product.price,
      stockQuantity: product.stockQuantity,
      category: product.category || 'VÊTEMENTS',
      imageUrl: product.imageUrl || ''
    };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  onSubmit(): void {
    if (this.isEditing && this.editingId) {
      this.productService.updateProduct(this.editingId, this.currentProduct).subscribe({
        next: () => {
          this.closeModal();
          this.loadProducts();
        },
        error: (err) => alert("Erreur lors de la modification du produit.")
      });
    } else {
      this.productService.createProduct(this.currentProduct).subscribe({
        next: () => {
          this.closeModal();
          this.loadProducts();
        },
        error: (err) => alert("Erreur lors de la création du produit.")
      });
    }
  }

  deleteProduct(id: number): void {
    if (!confirm('Êtes-vous sûr de vouloir supprimer ce produit ?')) return;

    this.productService.deleteProduct(id).subscribe({
      next: () => this.loadProducts(),
      error: (err) => alert("Erreur lors de la suppression du produit.")
    });
  }

  getStockBadgeColor(stock: number): string {
    if (stock === 0) return 'bg-red-500/10 text-red-500 border border-red-500/20';
    if (stock < 10) return 'bg-orange-500/10 text-orange-500 border border-orange-500/20';
    return 'bg-emerald-500/10 text-emerald-500 border border-emerald-500/20';
  }

  getStockBadgeText(stock: number): string {
    if (stock === 0) return 'Rupture';
    if (stock < 10) return 'Stock Faible';
    return 'En Stock';
  }
}
