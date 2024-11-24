import { Observable, of } from "rxjs";
import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from "@angular/router";
import { inject } from "@angular/core";
import { Raffle } from "../models/raffles/raffle";
import { RafflesService } from '../services/raffles/raffles.service';
import { TokenService } from "../services/token/token-service.service";

export const AdminRafflesResolver: ResolveFn<Raffle[] | undefined> = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
): Observable<Raffle[] | undefined> => {
    const tokenService: TokenService = inject(TokenService);
    const rafflesService: RafflesService = inject(RafflesService);
    const token = tokenService.getToken();
    if (!token || !tokenService.validate(token)) return of(undefined);
    return rafflesService.getAll();    
}