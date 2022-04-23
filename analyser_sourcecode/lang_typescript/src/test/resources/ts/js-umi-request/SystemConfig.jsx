import { querySystemInfo } from './system-info';

function TopicSetting(props) {
    useEffect(() => {
        querySystemInfo({ project: 'chapi'}).then((res) => {
            return res;
        });
    }, []);

    return (<PageContainer/>);
}
