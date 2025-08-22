import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router} from '@angular/router';
import { Store } from '@ngrx/store';
import {firstValueFrom, map} from 'rxjs';
import {selectIsAuthenticated, selectUserRole} from './store/auth/auth.selectors';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private store: Store, private router: Router) {}

  async canActivate(route: ActivatedRouteSnapshot): Promise<boolean> {
    const expectedRole = route.data['expectedRole'];

    const [isAuth, role] = await Promise.all([
      firstValueFrom(this.store.select(selectIsAuthenticated)),
      firstValueFrom(this.store.select(selectUserRole))
    ]);

    if (!isAuth) {
      this.router.navigate(['/login']);
      return false;
    }

    if (!role || role !== expectedRole) {
      this.router.navigate(['/login']);
      return false;
    }

    return true;
  }
}
