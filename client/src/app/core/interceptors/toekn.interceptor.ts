import { inject } from '@angular/core';
import { HttpRequest, HttpEvent, HttpInterceptorFn, HttpHandlerFn } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenService } from '../services/token/token-service.service';

export const tokenInterceptor: HttpInterceptorFn = (
    req: HttpRequest<any>,
    next: HttpHandlerFn
): Observable<HttpEvent<any>> => {

    const tokenService = inject(TokenService);
    const token = tokenService.getToken();

    if (!token) return next(req);

    const excludedUrls = [
        '/api/auth/authenticate',
        '/api/auth/register',
        'https://raffle-ease-bucket.s3.eu-north-1.amazonaws.com'
    ];

    if (excludedUrls.some(url => req.url.includes(url))) {
        return next(req);
    }

    const cloned = req.clone({
        setHeaders: {
            Authorization: `Bearer ${token}`
        },
    });

    return next(cloned);
};
