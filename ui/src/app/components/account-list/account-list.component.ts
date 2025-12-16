import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { Account } from '../../models/account.model';

@Component({
    selector: 'app-account-list',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './account-list.component.html',
    styleUrls: ['./account-list.component.css']
})
export class AccountListComponent implements OnInit {
    readonly accounts = signal<Account[]>([]);
    readonly loading = signal<boolean>(true);

    constructor(private accountService: AccountService) { }

    ngOnInit(): void {
        this.loadAccounts();
    }

    loadAccounts(): void {
        this.accountService.getAllAccounts().subscribe({
            next: (data) => {
                this.accounts.set(data);
                this.loading.set(false);
            },
            error: (err) => {
                console.error('Error loading accounts', err);
                this.loading.set(false);
            }
        });
    }
}
