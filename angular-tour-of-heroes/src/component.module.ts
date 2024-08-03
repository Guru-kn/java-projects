import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA, NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ChatComponent } from "./chat/chat.component";
import { NbActionsModule, NbButtonModule, NbChatCustomMessageService, NbChatModule, NbLayoutModule, NbSidebarModule, NbSidebarService, NbThemeModule, NbThemeService } from "@nebular/theme";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterModule, Routes } from "@angular/router";
import { ReactiveFormsModule } from "@angular/forms";
import { WebService } from "./services/http-service";
import { ChatService } from "./services/chat.service";
import { HttpClient, provideHttpClient } from "@angular/common/http";
import { UtilService } from "./util/util.service";

const routes: Routes = [
    {path: '', redirectTo: 'home', pathMatch: 'full'},
    {path: 'home', component: ChatComponent}
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes, { useHash: false }),
        BrowserModule,
        BrowserAnimationsModule,
        CommonModule,
        ReactiveFormsModule,
        NbLayoutModule,
        NbButtonModule,
        NbSidebarModule,
        NbChatModule,
        NbActionsModule,
        NbThemeModule.forRoot(),
    ],
    exports: [
        ChatComponent
    ],
    declarations: [
        ChatComponent
    ],
    providers: [
        NbThemeService,
        NbSidebarService,
        NbChatCustomMessageService,
        WebService,
        ChatService,
        UtilService,
        provideHttpClient(),
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
})

export class MyComponentModule {}