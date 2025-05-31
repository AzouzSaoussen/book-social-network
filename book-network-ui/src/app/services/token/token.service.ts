import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  set token(token: string){
    localStorage.setItem('token', token);
    console.log(token);
  }
  get token(){
    return localStorage.getItem('token') as string;
  }
}
