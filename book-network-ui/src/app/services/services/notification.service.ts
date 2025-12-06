import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';

export interface NotificationMessage {
  message: string;
  bookId: number;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private stompClient!: Client;

  connect(userId: number, onMessage: (msg: NotificationMessage) => void) {
    try {
      this.stompClient = new Client({
        webSocketFactory: () => new SockJS('http://localhost:8088/api/v1/ws'),
        reconnectDelay: 5000,
        debug: (msg: string) => console.log("STOMP DEBUG:", msg)
      });

      // 🔥 WebSocket ERROR events
      this.stompClient.onWebSocketError = (evt) => {
        console.error("❌ WebSocket error:", evt);
      };

      // 🔥 WebSocket CLOSE events
      this.stompClient.onWebSocketClose = (evt) => {
        console.warn("⚠️ WebSocket closed:", evt);
      };

      // 🔥 STOMP protocol errors
      this.stompClient.onStompError = (frame) => {
        console.error("❌ STOMP ERROR:", frame.headers["message"]);
        console.error("Details:", frame.body);
      };

      // 🔥 CONNECTED
      this.stompClient.onConnect = () => {
        console.log("✅ Connected to WebSocket server");

        const topic = `/topic/user/${userId}`;
        console.log("📌 Subscribing to:", topic);

        this.stompClient.subscribe(topic, (message: IMessage) => {
          console.log("📨 Notification received:", message.body);
          const body: NotificationMessage = JSON.parse(message.body);
          onMessage(body);
        });
      };

      // Start connection
      console.log("🔌 Activating WebSocket/Stomp client...");
      this.stompClient.activate();
    } catch (err) {
      console.error("🔥 Exception while connecting:", err);
    }
  }
}

