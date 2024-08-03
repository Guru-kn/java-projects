import { Injectable } from '@angular/core';
import { WebService } from './http-service';
import { Observable } from 'rxjs';

@Injectable()
export class ChatService {

    baseUrl: string = "http://localhost:8080";

    constructor(public webService: WebService){

    }

    public chat(text: string){
        return this.webService.getData(this.baseUrl + "/ai-chat/" + text, {responseType: "text"});
    }

    public readImageDataThroughAI(file: File, text: string): Observable<any> {
        const formData: FormData = new FormData();
        formData.append('file', file);
        formData.append('question', text);
    
        return this.webService.postWithFormData(this.baseUrl + "/gemini-pro-vision/ai-answer", formData, {responseType: "text"});
    }

    public generateAIImageFromText(text: string): Observable<any> {
        const formData: FormData = new FormData();
        formData.append('question', text);
    
        return this.webService.postData(this.baseUrl + "/ai-chat/" + text, {});
    }

}