import { Injectable } from "@angular/core";
import { TokenService } from "../services/token/token-service.service";
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from "@angular/router";

@Injectable({
    providedIn: 'root'
  })
  export class AuthGuard {
    constructor(
      private tokenService: TokenService,
      private router: Router
    ) {}  
  
    canActivate: CanActivateFn = (
      route: ActivatedRouteSnapshot,
      state: RouterStateSnapshot
    ): boolean => {
      const token = this.tokenService.getToken();
      if (token && this.tokenService.validate(token)) return true;
      this.router.navigate(['/admin/auth']);
      return false;
    }
  }