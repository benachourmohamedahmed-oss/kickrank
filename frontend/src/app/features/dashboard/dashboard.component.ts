import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CalendarPlus, Medal, ShieldCheck, UsersRound, LucideAngularModule } from 'lucide-angular';
import { AuthService } from '../../core/services/auth.service';

@Component({
  standalone: true,
  imports: [RouterLink, LucideAngularModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {
  readonly users = UsersRound;
  readonly medal = Medal;
  readonly shield = ShieldCheck;
  readonly calendar = CalendarPlus;

  constructor(public auth: AuthService) {}
}
