import { Component } from '@angular/core';
import { LoginFormComponent } from './login-form/login-form.component';
import { Router, RouterLink } from '@angular/router';
import { AuthRequest } from '../../../../../../core/models/auth/login-request';
import { AuthService } from '../../../../../../core/services/auth/auth.service';
import { TokenService } from '../../../../../../core/services/token/token-service.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [LoginFormComponent, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  constructor(
    private authService: AuthService,
    private tokenService: TokenService,
    private router: Router,
  ) { }

  login(authRequest: AuthRequest) {
    this.authService.authenticate(authRequest).subscribe({
      next: (token: string) => {
        this.tokenService.setToken(token);
        this.router.navigate(['/admin']);
      }
    })
  }
}
