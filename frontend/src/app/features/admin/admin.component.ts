import { Component, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ShieldCheck, LucideAngularModule } from 'lucide-angular';
import { Role } from '../../core/models/user.model';
import { RoleApplication, UserService } from '../../core/services/user.service';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, LucideAngularModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminComponent implements OnInit {
  readonly shield = ShieldCheck;
  readonly applications = signal<RoleApplication[]>([]);
  readonly message = signal<string | null>(null);
  readonly form = this.fb.nonNullable.group({
    requestedRole: ['ORGANIZER' as Role, Validators.required],
    motivation: ['', [Validators.required, Validators.minLength(20)]]
  });

  constructor(private fb: FormBuilder, private userService: UserService) {}

  ngOnInit(): void {
    this.load();
  }

  apply(): void {
    if (this.form.invalid) {
      return;
    }
    this.userService.applyForRole(this.form.getRawValue()).subscribe({
      next: () => {
        this.message.set('Candidature envoyee.');
        this.form.reset({ requestedRole: 'ORGANIZER', motivation: '' });
      },
      error: () => this.message.set('Impossible d envoyer la candidature.')
    });
  }

  load(): void {
    this.userService.pendingApplications().subscribe({
      next: (items) => this.applications.set(items),
      error: () => this.applications.set([])
    });
  }

  approve(id: string): void {
    this.userService.approve(id).subscribe(() => this.load());
  }

  reject(id: string): void {
    this.userService.reject(id).subscribe(() => this.load());
  }
}
