import { Component, OnInit, computed, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CalendarPlus, RefreshCcw, ShieldCheck, UsersRound, LucideAngularModule } from 'lucide-angular';
import { MatchService } from '../../core/services/match.service';
import { AuthService } from '../../core/services/auth.service';
import { Match, MatchFormat, MatchType } from '../../core/models/match.model';

@Component({
  standalone: true,
  imports: [DatePipe, ReactiveFormsModule, LucideAngularModule],
  templateUrl: './matches.component.html',
  styleUrl: './matches.component.scss'
})
export class MatchesComponent implements OnInit {
  readonly calendar = CalendarPlus;
  readonly users = UsersRound;
  readonly shield = ShieldCheck;
  readonly refresh = RefreshCcw;
  readonly matches = signal<Match[]>([]);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly canCreate = computed(() => this.auth.hasRole('ORGANIZER') || this.auth.hasRole('ADMIN'));

  readonly form = this.fb.nonNullable.group({
    title: ['', [Validators.required, Validators.minLength(4)]],
    location: ['', Validators.required],
    scheduledAt: ['', Validators.required],
    type: ['CASUAL' as MatchType, Validators.required],
    format: ['FIVE_V_FIVE' as MatchFormat, Validators.required],
    observerId: ['']
  });

  constructor(private fb: FormBuilder, private matchService: MatchService, public auth: AuthService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.matchService.findAll().subscribe({
      next: (matches) => {
        this.matches.set(matches);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossible de charger les matchs.');
        this.loading.set(false);
      }
    });
  }

  create(): void {
    if (this.form.invalid) {
      return;
    }
    const payload = this.form.getRawValue();
    this.matchService.create({
      ...payload,
      observerId: payload.observerId || null
    }).subscribe({
      next: () => {
        this.form.reset({ type: 'CASUAL', format: 'FIVE_V_FIVE', title: '', location: '', scheduledAt: '', observerId: '' });
        this.load();
      },
      error: () => this.error.set('Creation refusee. Verifiez le role organisateur et les donnees ranked.')
    });
  }

  join(match: Match): void {
    this.matchService.join(match.id).subscribe({
      next: () => this.load(),
      error: () => this.error.set('Inscription impossible pour ce match.')
    });
  }

  generateTeams(match: Match): void {
    this.matchService.generateTeams(match.id).subscribe({
      next: () => this.load(),
      error: () => this.error.set('La generation exige un match complet.')
    });
  }

  capacityLabel(match: Match): string {
    return `${match.participants.length}/${match.capacity}`;
  }
}
