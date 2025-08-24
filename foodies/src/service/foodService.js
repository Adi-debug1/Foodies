import axios from "axios";

const API_KEY = "http://localhost:8080/api/foods";

export const fetchFoodList =  async () => {
    try {
        const response = await axios.get(API_KEY);
        return response.data;
        
    } catch (error) {
        console.log('Error Fetching the food list', error);
        throw error;
    }
}

export const fetchFoodDetails = async (id) =>{
    try {
        const response = await axios.get(API_KEY+"/"+id);
        return response.data;
    } catch (error) {
        console.log('Error', error);
        throw error;
    }
}
       