import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { EurekaModule } from 'nestjs-eureka';
import { OpenAIModule } from '@platohq/nestjs-openai';
import { AutomatedResponseModule } from './modules/openai/automated-response-module';


@Module({
  imports: [
    // KeycloakModule, <-- commented because moved to the gateway
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: '.env',
    }),
    OpenAIModule.register({
      apiKey: 'sk-proj-w1ztpIwVl1hssHn4pNyKT3BlbkFJAaVWWThEoHiHGVfrsz8G',
    }),
    EurekaModule.forRootAsync({
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => {
        return {
          eureka: {
            host: configService.get<string>('EUREKA_HOST') ?? 'eureka-server',
            port: 8761,
            registryFetchInterval: 1000,
            servicePath: '/eureka/apps',
            maxRetries: 3,
          },
          service: {
            name: 'api',
            port: 4747,
            host: 'localhost',
          },
        };
      },
    }),
    AutomatedResponseModule,
  ],
  controllers: [],
  providers: [],
})
export class RootModule {}
