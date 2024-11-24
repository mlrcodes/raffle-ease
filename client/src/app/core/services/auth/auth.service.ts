import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { AuthRequest } from "../../models/auth/login-request";
import { Observable } from "rxjs";
import { RegisterRequest } from "../../models/auth/register-request";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(
        private httpClient: HttpClient
    ) { }

    private baseURL: string = 'http://localhost:8080/api/v1/auth';

    authenticate(authRequest: AuthRequest): Observable<string> {
        return this.httpClient.post(`${this.baseURL}/authenticate`, authRequest, {responseType: 'text'});
    }

    register(registerRequest: RegisterRequest): Observable<string> {
        return this.httpClient.post(`${this.baseURL}/register`, registerRequest, {responseType: 'text'});
    }
}  