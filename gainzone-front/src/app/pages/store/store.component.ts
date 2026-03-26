import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminProductService, Product } from '../../services/admin-product.service';
import { CartService } from '../../services/cart.service';
import { LucideAngularModule, ShoppingCart, Search, Package } from 'lucide-angular';
import { FloatingCartComponent } from '../../components/floating-cart/floating-cart.component';

@Component({
  selector: 'app-store',
  standalone: true,
  imports: [CommonModule, LucideAngularModule, FloatingCartComponent],
  templateUrl: './store.component.html'
})
export class StoreComponent implements OnInit {
  products = signal<Product[]>([]);
  activeCategory = signal<string>('Tous');
  isLoading = signal<boolean>(true);

  icons = { ShoppingCart, Search, Package };
  categories = ['Tous', 'VÊTEMENTS', 'ÉQUIPEMENT', 'NUTRITION', 'ACCESSOIRES'];

  filteredProducts = computed(() => {
    const all = this.products();
    const cat = this.activeCategory();
    if (cat === 'Tous') return all;
    return all.filter(p => p.category === cat);
  });

  constructor(
    private productService: AdminProductService,
    private cartService: CartService
  ) { }

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Erreur chargement boutique:', err);
        this.isLoading.set(false);
      }
    });
  }

  setCategory(cat: string): void {
    this.activeCategory.set(cat);
  }

  addToCart(product: Product): void {
    console.log('Produit ajouté au panier:', product);
    this.cartService.addToCart(product);
  }
}
