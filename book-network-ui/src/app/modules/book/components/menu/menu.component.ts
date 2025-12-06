import { Component } from '@angular/core';
import { TokenService } from "../../../../services/token/token.service";
import { NotificationService, NotificationMessage } from '../../../../services/services/notification.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent {

  sidebarOpen = false;
  username = 'User';
  notificationCount = 0;
  notifications: NotificationMessage[] = [];
  showNotifications = false;

  constructor(private tokenService: TokenService,
              private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.username = this.tokenService.fullName ?? 'User';
    const userId = this.tokenService.userId;
        if (userId) {
          this.notificationService.connect(userId, (msg) => this.onNotification(msg));
        }
  }
  onNotification(msg: NotificationMessage) {
    this.notifications.unshift(msg);      // store the message
    this.notificationCount++;             // increase badge counter
  }

  toggleNotifications() {
    this.showNotifications = !this.showNotifications;

    if (this.showNotifications) {
      this.notificationCount = 0;  // clear badge when opening list
    }
  }

  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }

  closeSidebar() {
    this.sidebarOpen = false;
  }

  logout() {
    localStorage.removeItem('token');
    window.location.reload();
  }
}


