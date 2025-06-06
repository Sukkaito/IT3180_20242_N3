import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import UserNavbar from '../../components/UserNavbar';
import { Subscription } from '../../data/subscriptions';
import { SubscriptionService } from '../../services/subscriptionService';
import { useAuth } from '../../hooks/useAuth';

export default function UserSubscriptions() {
    const [subscriptions, setSubscriptions] = useState<Subscription[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [processingId, setProcessingId] = useState<number | null>(null);
    const { userId } = useAuth();

    // Load subscriptions when the component mounts
    useEffect(() => {
        const loadSubscriptions = async () => {
            if (!userId) {
                setError('User authentication required');
                setLoading(false);
                return;
            }
            
            try {
                setLoading(true);
                const data = await SubscriptionService.getByUser(userId);
                setSubscriptions(data);
                setError(null);
            } catch (err) {
                console.error('Error loading subscriptions:', err);
                setError('Failed to load subscriptions. Please try again.');
            } finally {
                setLoading(false);
            }
        };

        loadSubscriptions();
    }, [userId]);

    // Handle unsubscribe action
    const handleUnsubscribe = async (subscriptionId: number) => {
        try {
            setProcessingId(subscriptionId);
            await SubscriptionService.unsubscribe(subscriptionId);
            // Remove the unsubscribed item from the list
            setSubscriptions(prevSubscriptions => 
                prevSubscriptions.filter(sub => sub.id !== subscriptionId)
            );
        } catch (err) {
            console.error('Error unsubscribing:', err);
            alert('Failed to unsubscribe. Please try again.');
        } finally {
            setProcessingId(null);
        }
    };

    // Show loading indicator
    if (loading) {
        return (
            <>
                <UserNavbar selected="subscriptions" />
                <div className="min-h-screen bg-blue-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-blue-600">Loading subscriptions...</p>
                    </div>
                </div>
            </>
        );
    }

    // Show error message
    if (error) {
        return (
            <>
                <UserNavbar selected="subscriptions" />
                <div className="min-h-screen bg-blue-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-red-600">{error}</p>
                        <button 
                            className="mt-4 bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700"
                            onClick={() => window.location.reload()}
                        >
                            Retry
                        </button>
                    </div>
                </div>
            </>
        );
    }

    return (
        <>
            <title>My Subscriptions</title>
            <UserNavbar selected="subscriptions" />
            
            <div className="min-h-screen bg-blue-50 p-6">
                <h2 className="text-2xl font-semibold text-blue-700 mb-4">My Subscriptions</h2>
                <p className="text-gray-700 mb-6">
                    Books you are subscribed to be notified when they become available.
                </p>
                
                <div className="bg-white rounded-lg shadow-md p-6">
                    {subscriptions.length === 0 ? (
                        <div className="text-center py-8">
                            <p className="text-gray-500">You don't have any active subscriptions.</p>
                            <Link to="/user/search" className="mt-4 inline-block text-blue-600 hover:underline">
                                Browse books to subscribe
                            </Link>
                        </div>
                    ) : (
                        <div className="space-y-4">
                            {subscriptions.map(subscription => (
                                <div key={subscription.id} className="border-b pb-4 flex justify-between items-center">
                                    <div>
                                        <h3 className="font-medium text-blue-800">{subscription.title}</h3>
                                    </div>
                                    <button
                                        className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 disabled:bg-red-300"
                                        onClick={() => handleUnsubscribe(subscription.id)}
                                        disabled={processingId === subscription.id}>
                                        {processingId === subscription.id ? 'Unsubscribing...' : 'Unsubscribe'}
                                    </button>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}
