import { map, Observable, switchMap } from "rxjs";
import { ActivatedRoute, ActivatedRouteSnapshot, ParamMap, ResolveFn, Router, RouterStateSnapshot } from "@angular/router";
import { inject } from "@angular/core";
import { Raffle } from "../models/raffles/raffle";
import { RafflesService } from '../services/raffles/raffles.service';

export const ClientRafflesResolver: ResolveFn<Observable<Raffle>> = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
): Observable<Raffle> => {
    const rafflesService: RafflesService = inject(RafflesService);
    const id = parseId(route.url[route.url.length - 1]?.path ?? '');
    return rafflesService.get(id); 
};

function parseId(raffleId: string): number {
    const parsedId = Number.parseInt(raffleId);
    return isNaN(parsedId) ? 0 : parsedId;
}
