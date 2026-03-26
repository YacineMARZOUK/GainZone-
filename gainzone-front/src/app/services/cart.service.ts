import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Product } from './admin-product.service';

export interface CartItem {
  product: Product;
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private http = inject(HttpClient);
  private router = inject(Router);
  
  cartItems = signal<CartItem[]>([]);

  totalItems = computed(() => {
    return this.cartItems().reduce((total, item) => total + item.quantity, 0);
  });

  totalPrice = computed(() => {
    return this.cartItems().reduce((total, item) => total + (item.product.price * item.quantity), 0);
  });

  constructor() { }

  addToCart(product: Product): void {
    this.cartItems.update(items => {
      const existingItem = items.find(item => item.product.id === product.id);

      if (existingItem) {
        if (existingItem.quantity < product.stockQuantity) {
          return items.map(item =>
            item.product.id === product.id
              ? { ...item, quantity: item.quantity + 1 }
              : item
          );
        }
        return items;
      } else {
        return [...items, { product, quantity: 1 }];
      }
    });
  }

  removeFromCart(productId: number): void {
    this.cartItems.update(items => items.filter(item => item.product.id !== productId));
  }

  updateQuantity(productId: number, delta: number): void {
    this.cartItems.update(items => {
      return items.map(item => {
        if (item.product.id === productId) {
          const newQuantity = item.quantity + delta;
          if (newQuantity >= 1 && newQuantity <= item.product.stockQuantity) {
            return { ...item, quantity: newQuantity };
          }
        }
        return item;
      });
    });
  }

  checkout(): void {
    if (this.cartItems().length === 0) return;

    const items = this.cartItems().map(item => ({
      productId: item.product.id,
      quantity: item.quantity
    }));

    this.http.post('http://localhost:8081/api/orders', { items }).subscribe({
      next: () => {
        alert('Commande validée avec succès ! 🎉');
        this.cartItems.set([]); 
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Erreur checkout:', err);
        alert('Erreur lors de la validation : ' + (err.error?.message || 'Vérifiez les stocks.'));
      }
    });
  }
}
