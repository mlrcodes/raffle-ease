import { Component, OnInit } from '@angular/core';
import { Stripe, loadStripe } from '@stripe/stripe-js';
import { catchError, switchMap, take } from 'rxjs/operators';
import { from, of } from 'rxjs';
import { StripeService } from '../../../../../core/services/stripe/stripe.service';
import { OrdersService } from '../../../../../core/services/orders/orders.service';
import { OrderRequest } from '../../../../../core/models/orders/order-request';
import { ShareOrdersService } from '../../../../../core/services/orders/share-orders.service';
import { CheckoutComponent } from './checkout/checkout.component';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CheckoutComponent],
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css'],
})
export class PaymentComponent implements OnInit {
  orderRequest!: OrderRequest;
  stripe!: Stripe | null;
  publicKey!: string;
  clientSecret!: string;

  constructor(
    private stripeService: StripeService,
    private ordersService: OrdersService,
    private shareOrders: ShareOrdersService
  ) {}

  ngOnInit(): void {
    this.initializePaymentFlow();
  }

  private initializePaymentFlow(): void {
    this.orderRequest = this.shareOrders.getOrder();

    this.stripeService
      .getPublicKey()
      .pipe(
        take(1),
        switchMap((publicKey: string) => {
          this.publicKey = publicKey;
          console.log(this.publicKey);
          return from(loadStripe(this.publicKey)).pipe(
            switchMap((stripe) => {
              if (!stripe) {
                throw new Error('Failed to initialize Stripe: ' + stripe);
              } 
              this.stripe = stripe;
              console.log(this.stripe);
              return this.ordersService.purchase(this.orderRequest);
            })
          );
          
        }),
        catchError((error: any) => {
          console.error('Error initializing payment flow:', error);
          return of(null); 
        })
      ).subscribe((clientSecret: string | null) => {
        if (clientSecret) {
          console.log(clientSecret);
          this.clientSecret = clientSecret;
        } else {
          console.error('Failed to retrieve client secret');
        }
      });
  }
}
