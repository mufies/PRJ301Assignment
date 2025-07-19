// async function getRoleFromJwt(token) {
//     if (!token) return null;
//
//     try {
//         const requestData = { getRole: token };
//         const response = await callJwtServlet(requestData);
//
//         if (response.error) {
//             console.error('Error getting role from JWT:', response.error);
//             return null;
//         }
//
//         return response.getRole;
//
//     } catch (error) {
//         console.error('Error calling JwtServlet:', error);
//         return null;
//     }
// }
//
// async function isJwtValid(token) {
//     if (!token) return false;
//
//     try {
//         const requestData = { isJwtValid: token };
//         const response = await callJwtServlet(requestData);
//
//         if (response.error) {
//             console.error('Error validating JWT:', response.error);
//             return false;
//         }
//
//         return response.isJwtValid;
//
//     } catch (error) {
//         console.error('Error calling JwtServlet:', error);
//         return false;
//     }
// }
//
// async function callJwtServlet(requestData) {
//     try {
//         const response = await fetch('JwtServlet', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(requestData)
//         });
//
//         if (!response.ok) {
//             throw new Error(`HTTP error! status: ${response.status}`);
//         }
//
//         return await response.json();
//
//     } catch (error) {
//         console.error('Error calling JwtServlet:', error);
//         return { error: error.message };
//     }
// }
