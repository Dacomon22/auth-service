import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { SsoCallbackComponent } from './features/auth/pages/sso-callback/sso-callback.component';

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'sso/callback', component: SsoCallbackComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}