import { Component } from '@angular/core';
import { RegisterFormComponent } from './register-form/register-form.component';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../../../core/services/auth/auth.service';
import { RegisterRequest } from '../../../../../../core/models/auth/register-request';
import { TokenService } from '../../../../../../core/services/token/token-service.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RegisterFormComponent, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  constructor(
    private authService: AuthService,
    private tokenService: TokenService,
    private router: Router
  ) {}

  register(registerRequest: RegisterRequest) {
    this.authService.register(registerRequest).subscribe({
      next: (token: string) => {
        this.tokenService.setToken(token);
        this.router.navigate(['/admin']);
      }
    })
  }
}
