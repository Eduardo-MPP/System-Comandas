import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class WebsocketService {
    private client: Client;
    private kitchenOrdersSubject = new BehaviorSubject<any>(null);
    private waiterNotificationsSubject = new BehaviorSubject<any>(null);

    public kitchenOrders$ = this.kitchenOrdersSubject.asObservable();
    public waiterNotifications$ = this.waiterNotificationsSubject.asObservable();

    constructor() {
        this.client = new Client({
            webSocketFactory: () => new SockJS(environment.wsUrl),
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000
        });
    }

    connect(userId?: number): void {
        this.client.onConnect = () => {
            console.log('WebSocket conectado');

            // Suscribirse a pedidos de cocina
            this.client.subscribe('/topic/kitchen', (message) => {
                this.kitchenOrdersSubject.next(JSON.parse(message.body));
            });

            // Suscribirse a notificaciones específicas del mesero
            if (userId) {
                this.client.subscribe(`/topic/waiter/${userId}`, (message) => {
                    const order = JSON.parse(message.body);
                    this.waiterNotificationsSubject.next(order);
                    console.log('Pedido listo recibido:', order);
                });
            }
        };

        this.client.activate();
    }

    disconnect(): void {
        if (this.client.active) {
            this.client.deactivate();
        }
    }
}
