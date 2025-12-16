import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { timer } from 'rxjs';
import { AccountService } from '../../services/account.service';

@Component({
    selector: 'app-create-account',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterModule],
    templateUrl: './create-account.component.html',
    styleUrls: ['./create-account.component.css']
})
export class CreateAccountComponent {
    accountForm: FormGroup;
    readonly isSubmitting = signal<boolean>(false);
    readonly error = signal<string>('');

    constructor(
        private fb: FormBuilder,
        private accountService: AccountService,
        private router: Router
    ) {
        this.accountForm = this.fb.group({
            balance: [0, [Validators.required, Validators.min(0)]],
            currency: ['MAD', [Validators.required]]
        });
    }

    onSubmit(): void {
        if (this.accountForm.valid) {
            this.isSubmitting.set(true);
            this.error.set('');

            this.accountService.createAccount(this.accountForm.value).subscribe({
                next: (response) => {
                    // The response is the ID string
                    console.log('Account created:', response);
                    // Delay navigation to allow Event Store -> Read Model propagation
                    timer(500).subscribe(() => this.router.navigate(['/accounts']));
                },
                error: (err) => {
                    console.error('Error creating account', err);
                    this.error.set('Failed to create account. Please try again.');
                    this.isSubmitting.set(false);
                }
            });
        }
    }
}
