import { Routes } from '@angular/router';
import { AccountListComponent } from './components/account-list/account-list.component';
import { CreateAccountComponent } from './components/create-account/create-account.component';
import { AccountDetailsComponent } from './components/account-details/account-details.component';

export const routes: Routes = [
    { path: '', redirectTo: 'accounts', pathMatch: 'full' },
    { path: 'accounts', component: AccountListComponent },
    { path: 'create-account', component: CreateAccountComponent },
    { path: 'account/:id', component: AccountDetailsComponent },
    { path: '**', redirectTo: 'accounts' }
];
