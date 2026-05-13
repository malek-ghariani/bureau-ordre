// src/app/shared/shared.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../components/sidebar/sidebar.component';
import { NavbarComponent } from '../components/navbar/navbar.component';
import { FooterComponent } from '../components/footer/footer.component';
import { MaterialModule } from '../material.module';

@NgModule({
  declarations: [SidebarComponent, NavbarComponent, FooterComponent],
  imports: [CommonModule, MaterialModule],
  exports: [SidebarComponent, NavbarComponent, FooterComponent]
})
export class SharedModule {}
