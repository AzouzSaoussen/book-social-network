import { Component } from '@angular/core';
import {AuthenticationService} from "../../services/services/authentication.service";
import {Router} from "@angular/router";


@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.scss']
})
export class ActivateAccountComponent {
  message = '';
  isOkay = false;
  submitted = false;
  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {}

  private confirmAccount(token: string) {
    console.log('Sending token:', token);
    this.authService.confirm({
      token
    }).subscribe({
      next: () => {
        console.log('in the next');
        this.message = 'Your account has been successfully activated.\nNow you can proceed to login';
        this.submitted = true;
        this.isOkay = true;
      },
      error: () => {
        console.log('in the error');
        this.message = 'Token has been expired or invalid';
        this.submitted = true;
        this.isOkay = false;
      }
    });
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

}
