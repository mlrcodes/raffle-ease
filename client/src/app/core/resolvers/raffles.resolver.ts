import { Observable } from "rxjs";
import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from "@angular/router";
import { inject } from "@angular/core";
import { Raffle } from "../models/raffles/raffle";
import { RafflesService } from '../services/raffles/raffles.service';

export const RafflesResolver: ResolveFn<Raffle[]> = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
): Observable<Raffle[]> => {
    const rafflesService: RafflesService = inject(RafflesService);
    return rafflesService.getAll(1)
}