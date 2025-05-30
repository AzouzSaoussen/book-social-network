import { Component } from '@angular/core';
import {AuthenticationRequest} from '../../services/models/authentication-request';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {TokenService} from "../../services/token/token.service";
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
              private tokenService: TokenService) {

  }
  login(): void {
    this.errorMsg = [];

    this.authService.authenticate({ body: this.authRequest }).subscribe({
      next: async (res) => {
        try {
          // Save token securely
          this.tokenService.token = res.token as string;

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
