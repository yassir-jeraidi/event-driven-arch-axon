export interface Account {
  id: string;
  balance: number;
  status: 'CREATED' | 'SUSPENDED' | 'ACTIVATED' | 'BLOCKED';
  currency: 'MAD' | 'USD' | 'EUR';
  createdDate: string;
  transactions?: Transaction[];
}

export interface Transaction {
  id: string;
  date: string;
  amount: number;
  currency: 'MAD' | 'USD' | 'EUR';
  type: 'CREDIT' | 'DEBIT';
}

export interface CreateAccountRequest {
  balance: number;
  currency: 'MAD' | 'USD' | 'EUR';
}

export interface CreditAccountRequest {
  id: string;
  amount: number;
  currency: 'MAD' | 'USD' | 'EUR';
}

export interface DebitAccountRequest {
  id: string;
  amount: number;
  currency: 'MAD' | 'USD' | 'EUR';
}
