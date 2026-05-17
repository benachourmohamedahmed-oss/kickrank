import { Component, OnInit, signal } from '@angular/core';
import { Medal, LucideAngularModule } from 'lucide-angular';
import { UserSummary } from '../../core/models/user.model';
import { UserService } from '../../core/services/user.service';

@Component({
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.scss'
})
export class LeaderboardComponent implements OnInit {
  readonly medal = Medal;
  readonly users = signal<UserSummary[]>([]);

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.leaderboard().subscribe((users) => this.users.set(users));
  }
}
