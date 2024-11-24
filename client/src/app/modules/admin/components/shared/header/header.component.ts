import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TokenService } from '../../../../../core/services/token/token-service.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  constructor(
    private tokenService: TokenService,
    private router: Router
  ) {}

  logout() {
    this.tokenService.logout();
    this.router.navigate(['/admin/auth'])
  }

}
