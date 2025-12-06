import { Injectable } from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  set token(token: string){
    localStorage.setItem('token', token);
  }
  get token(){
    return localStorage.getItem('token') as string;
  }
  get userId(): number | null {
    const token = this.token;
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.userId ?? null;
    } catch {
      return null;
    }
  }
  get fullName(): string | null {
    const token = this.token;
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.fullName ?? null;
    } catch {
      return null;
    }
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }

  private isTokenValid() {
    const token = this.token;
    if(!token){
      return false;
    }
    //decode the token
    const jwtHelper = new JwtHelperService();
    // check expiry date
    const isTokenExpired= jwtHelper.isTokenExpired(token);
    if(isTokenExpired){
      localStorage.clear();
      return false;
    }
    return true;
  }
}
