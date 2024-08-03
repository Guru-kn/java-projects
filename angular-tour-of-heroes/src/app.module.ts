import { NgModule } from '@angular/core';
import { AppComponent } from './app/app.component';
import { MyComponentModule } from './component.module';


@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        MyComponentModule
    ],
    providers: [
        
    ],
    bootstrap: [AppComponent],
})
export class AppModule { }