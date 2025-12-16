import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Account, CreateAccountRequest, CreditAccountRequest, DebitAccountRequest } from '../models/account.model';

@Injectable({
    providedIn: 'root'
})
export class AccountService {
    private baseUrl = 'http://localhost:8080';

    private accountsCache: Account[] | null = null;

    constructor(private http: HttpClient) { }

    // Queries
    getAllAccounts(): Observable<Account[]> {
        if (this.accountsCache) {
            return of(this.accountsCache);
        }
        return this.http.get<Account[]>(`${this.baseUrl}/query/account`).pipe(
            tap(data => this.accountsCache = data)
        );
    }

    getAccountById(id: string): Observable<Account> {
        return this.http.get<Account>(`${this.baseUrl}/query/account/${id}`);
    }

    // Commands
    createAccount(account: CreateAccountRequest): Observable<string> {
        return this.http.post(`${this.baseUrl}/command/account`, account, { responseType: 'text' }).pipe(
            tap(() => this.accountsCache = null)
        );
    }

    creditAccount(request: CreditAccountRequest): Observable<string> {
        return this.http.post(`${this.baseUrl}/command/account/credit`, request, { responseType: 'text' }).pipe(
            tap(() => this.accountsCache = null)
        );
    }

    debitAccount(request: DebitAccountRequest): Observable<string> {
        return this.http.post(`${this.baseUrl}/command/account/debit`, request, { responseType: 'text' }).pipe(
            tap(() => this.accountsCache = null)
        );
    }

    // Actions
    activateAccount(id: string): Observable<string> {
        return this.http.post(`${this.baseUrl}/command/account/${id}/activate`, {}, { responseType: 'text' }).pipe(
            tap(() => this.accountsCache = null)
        );
    }

    suspendAccount(id: string): Observable<string> {
        return this.http.post(`${this.baseUrl}/command/account/${id}/suspend`, {}, { responseType: 'text' }).pipe(
            tap(() => this.accountsCache = null)
        );
    }

    blockAccount(id: string): Observable<string> {
        return this.http.post(`${this.baseUrl}/command/account/${id}/block`, {}, { responseType: 'text' }).pipe(
            tap(() => this.accountsCache = null)
        );
    }
}
