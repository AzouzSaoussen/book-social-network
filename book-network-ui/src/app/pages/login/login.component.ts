import { Component } from '@angular/core';
import {AuthenticationRequest} from '../../services/models/authentication-request';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {TokenService} from "../../services/token/token.service";
import { NotificationService, NotificationMessage } from '../../services/services/notification.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  authRequest: AuthenticationRequest ={email:'', password:'' };
  errorMsg: Array<string> =[];
  constructor(private router: Router,
              private authService: AuthenticationService,
              private tokenService: TokenService,
              private notifications: NotificationService) {

  }
  login(): void {
    this.errorMsg = [];

    this.authService.authenticate({ body: this.authRequest }).subscribe({
      next: async (res) => {
        try {
          // Save token securely
          this.tokenService.token = res.token as string;
          // Connect notifications after login
          const userId = this.tokenService.userId;
          console.log("the connected user id is {}", userId);
          if (userId) {
            console.log("lets start socket connection")
            this.notifications.connect(userId, (payload: NotificationMessage) => {
              alert(`Message: ${payload.message}\nBook ID: ${payload.bookId}`);
            });
          }
          // Optional: Show success toast or spinner here
          await this.router.navigate(['books']);
          // Optional: success handler
          console.log("Navigation to /books successful");

        } catch (navigationError) {
          console.error("Navigation to /books failed:", navigationError);
          this.errorMsg.push("Login succeeded but navigation failed. Please try again.");
        }
      },
      error: (err) => {
        console.error("Authentication failed:", err);
        if (err.error?.validationErrors?.length) {
          this.errorMsg = err.error.validationErrors;
        } else if (err.error?.error) {
          this.errorMsg.push(err.error.error);
        } else {
          this.errorMsg.push("Unexpected error occurred. Please try again later.");
        }
      }
    });
  }
  register(){
    this.router.navigate(['register'])
  }
}
