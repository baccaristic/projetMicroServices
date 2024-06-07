import { Injectable } from '@nestjs/common';
import { OpenAIClient } from '@platohq/nestjs-openai';
import OpenAI from 'openai';
import axios from 'axios';

@Injectable()
export class OpenaiService {
  constructor(private readonly openAIClient: OpenAIClient) {}

  getHello(): string {
    return 'Hello World!';
  }

   openai = new OpenAI({
    apiKey: "pk-KzuQqaPRRXGwXeKADrfGBTpFOumGUcYEnwUQkVDNCZFxnEIH",
    baseURL: "https://api.pawan.krd/v1/chat/completions",
  });

  createPrompt(text: string) {
    return `
      I want you to act as a customer support agent. Your role is to give a generic answer to the complaint, ask for more details, and ask the user to be patient.
      Here is the complaint:
      _________
      ${text}
      _________
      i want you to answer the complaint in a way that is generic and does not require any specific knowledge about the product or service.
      Just give a generic answer, ask for more details, and ask the user to be patient.
      In your response, just give me the answer to the complaint, without saying anything else.
      `;
  }

  async getAnswerForComplaint(text: string) {
    const authToken = 'pk-KzuQqaPRRXGwXeKADrfGBTpFOumGUcYEnwUQkVDNCZFxnEIH';

    const data = {
      model: 'pai-001-light',
      max_tokens: 100,
      messages: [
        {
          role: 'user',
          content: this.createPrompt(text)
        }
      ]
    };

    const response =  await axios.post('https://api.pawan.krd/v1/chat/completions', data, {
      headers: {
        'Authorization': `Bearer ${authToken}`,
        'Content-Type': 'application/json'
      }
    });
    return response.data.choices[0].message.content;
  }
}
