import {API_HOST_PATH} from '../../environments/environment';

export const API_BASE_PATH = API_HOST_PATH + '/api/1.0';

export const API_AUTHENTICATION = API_BASE_PATH + '/authentication';
export const API_RESTAURANT = API_BASE_PATH + '/restaurants';
export const API_USER = API_BASE_PATH + '/users';
export const API_REVIEW = API_RESTAURANT + '/{restaurantId}/reviews';

export const PATH_TOKEN = '/token';
export const PATH_REGISTER = '/register';
