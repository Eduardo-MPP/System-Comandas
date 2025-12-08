import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { RoleType } from '../models/user.model';

@Injectable({
    providedIn: 'root'
})
export class RoleGuard implements CanActivate {
    constructor(private authService: AuthService, private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot): boolean {
        const expectedRole = route.data['role'] as RoleType;

        if (this.authService.hasRole(expectedRole)) {
            return true;
        }

        this.router.navigate(['/login']);
        return false;
    }
}
