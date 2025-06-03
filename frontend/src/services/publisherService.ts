import { BaseService } from './baseService';
import { Publisher } from '../data/publishers';

// Create publisher service using the base service
const publisherBaseService = new BaseService<Publisher>('publishers');

// Publisher service object that wraps all API functions
export const publisherService = {
    getAll: () => publisherBaseService.getAll(),
    create: (publisherData: Partial<Publisher>) => publisherBaseService.create(publisherData),
    update: (id: string, publisherData: Partial<Publisher>) => publisherBaseService.update(id, publisherData),
    delete: (id: string) => publisherBaseService.delete(id)
};
