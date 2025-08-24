import axios from "axios";
import { createContext, useEffect, useState } from "react";
import { fetchFoodList } from "../service/foodService";
import { addToCart, getCartData, removeQtyFromCart } from "../service/cartService";

export const StoreContext = createContext(null);

export const StoreContextProvider = (props) => {

    const [foodList, setFoodList] = useState([]);
    const [quantities, setQuantities] = useState({});
    const [token, setToken] = useState("");

    const increaseQty = async (foodId) => {
        setQuantities((prev) => ({...prev, [foodId]: (prev[foodId] || 0) + 1}));
        addToCart(foodId, token);

    }
    const decreaseQty = async (foodId) => {
        setQuantities((prev) => ({...prev, [foodId]: prev[foodId] >= 0 ? prev[foodId]-1 : 0}));
        removeQtyFromCart(foodId, token);
    }

    const removeFromCart = (foodId) => {
        setQuantities((prevQunatities) => {
            const updatedQuantites = {...prevQunatities};
            delete updatedQuantites[foodId];
            return updatedQuantites;
        })
    }

    const loadCartData = async (token) => {
        const item = await getCartData(token);
        setQuantities(item);
    }

     const contextValue = {
        foodList,
        increaseQty,
        decreaseQty,
        quantities,
        setQuantities,
        removeFromCart,
        token,
        setToken,
        loadCartData
    };

    useEffect(() => {
        async function loadData(){
           const data = await fetchFoodList();
           setFoodList(data);
           if(localStorage.getItem("token")){
            setToken(localStorage.getItem("token"));
            await loadCartData(localStorage.getItem("token"));
           }
        }
        loadData();
    },[]); 

    return (

        <StoreContext.Provider value={contextValue}>
            {props.children}
        </StoreContext.Provider>
    )
}