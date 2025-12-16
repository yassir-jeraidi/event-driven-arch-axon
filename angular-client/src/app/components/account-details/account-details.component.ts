import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AccountService } from '../../services/account.service';
import { timer } from 'rxjs';
import { Account } from '../../models/account.model';

@Component({
    selector: 'app-account-details',
    standalone: true,
    imports: [CommonModule, RouterModule, FormsModule],
    templateUrl: './account-details.component.html',
    styleUrls: ['./account-details.component.css']
})
export class AccountDetailsComponent implements OnInit {
    readonly account = signal<Account | null>(null);
    readonly loading = signal<boolean>(true);
    operationAmount: number = 0;
    operationType: 'CREDIT' | 'DEBIT' = 'CREDIT';
    readonly operationLoading = signal<boolean>(false);

    constructor(
        private route: ActivatedRoute,
        private accountService: AccountService
    ) { }

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            const id = params.get('id');
            console.log('Route params:', params);
            console.log('Account ID:', id);
            if (id) {
                this.loadAccount(id);
            } else {
                console.error('No account ID found in route');
                this.loading.set(false);
            }
        });
    }

    loadAccount(id: string): void {
        this.loading.set(true);
        this.accountService.getAccountById(id).subscribe({
            next: (data) => {
                this.account.set(data);
                this.loading.set(false);
            },
            error: (err) => {
                console.error('Error loading account', err);
                this.loading.set(false);
            }
        });
    }

    onOperationSubmit(): void {
        const currentAccount = this.account();
        if (!currentAccount || this.operationAmount <= 0) return;

        this.operationLoading.set(true);
        const request = {
            id: currentAccount.id,
            amount: this.operationAmount,
            currency: currentAccount.currency
        };

        const operation$ = this.operationType === 'CREDIT'
            ? this.accountService.creditAccount(request)
            : this.accountService.debitAccount(request);

        operation$.subscribe({
            next: () => {
                this.operationAmount = 0;
                this.operationLoading.set(false);
                // Delay reload to allow Event Store -> Read Model propagation
                timer(500).subscribe(() => this.loadAccount(currentAccount.id));
            },
            error: (err) => {
                console.error('Operation failed', err);
                this.operationLoading.set(false);
            }
        });
    }

    changeStatus(status: 'ACTIVATED' | 'SUSPENDED' | 'BLOCKED'): void {
        const currentAccount = this.account();
        if (!currentAccount) return;

        let action$;
        switch (status) {
            case 'ACTIVATED':
                action$ = this.accountService.activateAccount(currentAccount.id);
                break;
            case 'SUSPENDED':
                action$ = this.accountService.suspendAccount(currentAccount.id);
                break;
            case 'BLOCKED':
                action$ = this.accountService.blockAccount(currentAccount.id);
                break;
        }

        action$.subscribe({
            next: () => {
                // Delay reload to allow Event Store -> Read Model propagation
                timer(500).subscribe(() => this.loadAccount(currentAccount.id));
            },
            error: (err) => console.error('Status change failed', err)
        });
    }
}
