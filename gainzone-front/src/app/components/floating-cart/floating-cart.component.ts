import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../../services/cart.service';
import { LucideAngularModule, ShoppingCart, X, Plus, Minus, Trash2, Package } from 'lucide-angular';

@Component({
  selector: 'app-floating-cart',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './floating-cart.component.html'
})
export class FloatingCartComponent {
  isOpen = signal(false);

  icons = { ShoppingCart, X, Plus, Minus, Trash2, Package };

  constructor(public cartService: CartService) {}

  toggleCart() {
    this.isOpen.update(val => !val);
  }
}
