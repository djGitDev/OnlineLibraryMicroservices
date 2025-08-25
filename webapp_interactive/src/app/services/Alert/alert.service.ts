import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor(private snackBar: MatSnackBar) {}

  success(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }


  error(err: any) {
    console.error('Register error:', err);

    const backendMsg: string = err?.error?.error || 'Unknown error';
    const lowerMsg = backendMsg.toLowerCase();
    let userMessage = backendMsg;

    if (lowerMsg.includes('duplicate') && lowerMsg.includes('users')) {
      userMessage = '⛔ User already exists with this email';
    }
    else if (lowerMsg.includes('duplicate') && lowerMsg.includes('book')) {
      userMessage = '⛔ Book already exists with same ISBN in the catalog';
    }
    else if (lowerMsg.includes('duplicate')) {
      userMessage = '⛔ Duplicate entry detected';
    }
    else {
      userMessage =  '⛔'+ backendMsg;
    }

    this.snackBar.open(userMessage, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }
    alert(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['alert-snackbar']
    });
  }
}
