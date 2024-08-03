import { Component, OnDestroy, OnInit } from "@angular/core";
import { ChatService } from "../services/chat.service";
import { UtilService } from "../util/util.service";


@Component({
    selector: 'app-chat',
    templateUrl: './chat.component.html',
    styleUrl: './chat.component.scss'
})
export class ChatComponent implements OnInit, OnDestroy {

    messages: any = [];

    data: any = { href: 'https://example.com', text: 'example.com' };

    ngOnInit(): void {

    }

    ngOnDestroy(): void {

    }

    constructor(public chatService: ChatService, public utilService: UtilService) {

    }

    sendMessage(data: any) {

        this.pushUserMessageToMessages(data);

        if(data.files && data.files.length > 0) {
            this.chatService.readImageDataThroughAI(data.files[0],data.message).subscribe((response: any) => {
                this.pushAIMessageToMessages(response);
            },
                error => console.error(error)
            );
        } else if(data.message && data.message.toString().toLocaleLowerCase().startsWith("generate")) {
            this.chatService.generateAIImageFromText(data.message).subscribe((response: any) => {
                response.chatResponse = "Your image will be downloaded in few seconds";
                this.pushAIMessageToMessages(response, true);
                this.utilService.downloadImageFromByteArray(response.generatedImage, response.mimeType);
            },
                error => console.error(error)
            );
        } else {
            this.chatService.chat(data.message).subscribe((response: any) => {
                this.pushAIMessageToMessages(response);
            },
                error => console.error(error)
            );
        }

        
    }

    buildFileData(file: any){

        const imgDetails: any = {
            url: file.src, 
            type: file.type,
            icon: "file-text-outline"
        }

        return [imgDetails];
    }

    pushUserMessageToMessages(data: any) {

        const hasFileAttachment: boolean = (data.files && data.files.length > 0) ? true : false;

        this.messages.push({
            type: hasFileAttachment ? 'file' : 'text',
            text: data.message,
            user: {
                name: "Guru",
                avatar: 'https://i.gifer.com/no.gif'
            },
            date: new Date().toDateString(),
            files: hasFileAttachment ? this.buildFileData(data.files[0]) : [],
            quote: "",
            latitude: "",
            longtitude: "",
            reply: true
        })
    }

    pushAIMessageToMessages(data: any, isImage: boolean = false) {

        let imgDetails: any = {};

        if(isImage) {
            imgDetails = [{
                url: 'data:image/png;base64,' + data.generatedImage, 
                type: data.mimeType,
                icon: "file-text-outline"
            }]
        }

        this.messages.push({
            type: isImage ? "file" : "text",
            text: isImage ? data.chatResponse : data.replace(/\*\*(.*?)\*\*/g, "<b>$1</b>"),
            user: {
                name: "BOT",
                avatar: 'bot.png'
            },
            date: new Date().toDateString(),
            files: isImage ? imgDetails : [],
            quote: "",
            latitude: "",
            longtitude: ""
        })
    }
}